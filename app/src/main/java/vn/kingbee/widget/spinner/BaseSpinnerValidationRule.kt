package vn.kingbee.widget.spinner

abstract class BaseSpinnerValidationRule(var errorMessage: String) {

    abstract fun isValid(materialVasSpinner: MaterialSpinner, position: Int): Boolean
}