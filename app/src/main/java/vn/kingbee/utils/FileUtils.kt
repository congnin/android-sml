package vn.kingbee.utils

import java.io.File

class FileUtils {
    companion object {
        fun concatDirectoryPath(parentDirectory: String, childDirectory: String): String? {
            if (parentDirectory.isEmpty() && childDirectory.isEmpty()) {
                return null
            } else if (parentDirectory.isEmpty()) {
                return childDirectory
            } else if (childDirectory.isEmpty()) {
                return parentDirectory
            }

            return File(parentDirectory, childDirectory).path
        }
    }
}