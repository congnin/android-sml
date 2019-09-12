package vn.kingbee.achilles.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okio.Buffer;
import timber.log.Timber;
import vn.kingbee.achilles.model.EndpointElement;

public class Utils {
    public static final String TAG = "Achilles";
    private static final Charset UTF8 = Charset.forName("UTF-8");

    private Utils() {
    }

    @TargetApi(19)
    @NonNull
    public static String readAsset(Context ctx, String fileName) {
        AssetManager assetManager = ctx.getAssets();

        try {
            InputStream inputStream = assetManager.open(fileName);
            Throwable var4 = null;

            try {
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                Throwable var6 = null;

                try {
                    byte[] e = new byte[1024];

                    int len;
                    while((len = inputStream.read(e)) != -1) {
                        outputStream.write(e, 0, len);
                    }

                    String var9 = outputStream.toString();
                    return var9;
                } catch (Throwable var34) {
                    var6 = var34;
                    throw var34;
                } finally {
                    if (outputStream != null) {
                        if (var6 != null) {
                            try {
                                outputStream.close();
                            } catch (Throwable var33) {
                                var6.addSuppressed(var33);
                            }
                        } else {
                            outputStream.close();
                        }
                    }

                }
            } catch (Throwable var36) {
                var4 = var36;
                throw var36;
            } finally {
                if (inputStream != null) {
                    if (var4 != null) {
                        try {
                            inputStream.close();
                        } catch (Throwable var32) {
                            var4.addSuppressed(var32);
                        }
                    } else {
                        inputStream.close();
                    }
                }

            }
        } catch (IOException var38) {
            Timber.tag("Achilles").e(var38.getMessage(), new Object[0]);
            return "";
        }
    }

    @TargetApi(19)
    public static String readSdCardFile(String url) {
        StringBuilder textBuilder = new StringBuilder();
        File textFile = new File(url);

        try {
            BufferedReader reader = new BufferedReader(new FileReader(textFile));
            Throwable var4 = null;

            try {
                String line;
                try {
                    while((line = reader.readLine()) != null) {
                        textBuilder.append(line);
                        textBuilder.append("\n");
                    }
                } catch (Throwable var14) {
                    var4 = var14;
                    throw var14;
                }
            } finally {
                if (reader != null) {
                    if (var4 != null) {
                        try {
                            reader.close();
                        } catch (Throwable var13) {
                            var4.addSuppressed(var13);
                        }
                    } else {
                        reader.close();
                    }
                }

            }
        } catch (IOException var16) {
            Timber.tag("Achilles").e(var16.getMessage(), new Object[0]);
        }

        return textBuilder.toString();
    }

    public static String getSdCardFileNameFromPrefix(String path, String fileName) {
        String result = null;
        String[] fileNames = (new File(path)).list();
        if (fileNames.length > 0) {
            result = fileNames[0];
        }

        String[] var4 = fileNames;
        int var5 = fileNames.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            String file = var4[var6];
            if (file.startsWith(fileName)) {
                result = file;
                break;
            }
        }

        return result;
    }

    public static String getAssetFileNameFromPrefix(Context context, String path, String fileName) {
        String result = null;

        try {
            String[] fileNames = context.getAssets().list(path);
            String[] var5 = fileNames;
            int var6 = fileNames.length;

            int var7;
            String file;
            for(var7 = 0; var7 < var6; ++var7) {
                file = var5[var7];
                if (file.equalsIgnoreCase(fileName + ".json")) {
                    result = file;
                    break;
                }
            }

            if (result == null) {
                var5 = fileNames;
                var6 = fileNames.length;

                for(var7 = 0; var7 < var6; ++var7) {
                    file = var5[var7];
                    if (file.startsWith(fileName)) {
                        result = file;
                        break;
                    }
                }
            }

            if (fileNames.length > 0 && result == null) {
                result = fileNames[0];
            }
        } catch (IOException var9) {
            Timber.tag("Achilles").e(var9.getMessage(), new Object[0]);
        }

        return result;
    }

    public static byte[] readBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len;
        while((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }

        return byteBuffer.toByteArray();
    }

    public static String getDataResponseFilePathFromEndpointConfig(List<EndpointElement.Data> dataField, Map<String, String> dataList, String apiFileName) {
        StringBuilder dataString = new StringBuilder();
        Iterator var4 = dataField.iterator();

        while(var4.hasNext()) {
            EndpointElement.Data field = (EndpointElement.Data)var4.next();
            if (dataList.get(field.getDataField()) != null && dataList.size() > 0 && !((String)dataList.get(field.getDataField())).equalsIgnoreCase("null")) {
                dataString.append(String.format("%s_", dataList.get(field.getDataField())));
            }
        }

        return StringUtils.INSTANCE.isEmpty(dataString) ? apiFileName : String.format("%s_%s", apiFileName, dataString.substring(0, dataString.lastIndexOf("_")));
    }

    @NonNull
    public static String getApiFileNameFromRequest(Request request) throws MalformedURLException {
        HttpUrl requestedUrl = request.url();
        return getNonEmptyName(requestedUrl.pathSegments(), requestedUrl.pathSize() - 1);
    }

    static String getNonEmptyName(@NonNull List<String> list, int index) {
        if (index < 0) {
            return "";
        } else {
            String rs = (String)list.get(index);
            return rs != null && !rs.isEmpty() ? rs : getNonEmptyName(list, index - 1);
        }
    }

    public static String getRequestBodyStringFromInterceptorChain(Request request) throws IOException {
        RequestBody requestBody = request.body();
        Buffer buffer = new Buffer();
        requestBody.writeTo(buffer);
        Charset charset = UTF8;
        MediaType contentType = requestBody.contentType();
        if (contentType != null) {
            charset = contentType.charset(UTF8);
        }

        return buffer.readString(charset);
    }
}
