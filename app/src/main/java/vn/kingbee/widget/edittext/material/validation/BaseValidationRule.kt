package vn.kingbee.widget.edittext.material.validation

abstract class BaseValidationRule(var errorMessage: String) {

    abstract fun isValid(input: String): Boolean
}