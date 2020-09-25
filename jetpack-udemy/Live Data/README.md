# Summary

## setValue() or postValue()
There are two methods to update the value of a MutableLiveData instance.

setValue() and the postValue().

This setvalue() method must be called from the main thread.

If you need to set a value form a background thread you should use the postValue() method.