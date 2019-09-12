package vn.kingbee.rxjava.model


class User {
    var id: Long? = null
    var firstName: String? = null
    var lastName: String? = null
    var isFollowing: Boolean? = null

    constructor()

    constructor(apiUser: ApiUser) {
        this.id = apiUser.id
        this.firstName = apiUser.firstName
        this.lastName = apiUser.lastName
    }

    override fun toString(): String =
        "User{id=$id, firstName='$firstName', lastName='$lastName', isFollowing=$isFollowing}"


    override fun hashCode(): Int = (id!! + firstName.hashCode() + lastName.hashCode()).toInt()

    override fun equals(other: Any?): Boolean {
        if (other is User) {

            return (this.id == other.id && this.firstName.equals(other.firstName) && this.lastName.equals(
                other.lastName
            ))
        }

        return false
    }

}