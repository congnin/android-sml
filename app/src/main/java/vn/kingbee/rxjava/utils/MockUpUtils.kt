package vn.kingbee.rxjava.utils

import vn.kingbee.rxjava.model.ApiUser
import vn.kingbee.rxjava.model.User


class MockUpUtils {
    companion object {
        fun getUserList(): List<User> {

            val userList = ArrayList<User>()

            val userOne = User()
            userOne.firstName = "Amit"
            userOne.lastName = "Shekhar"
            userList.add(userOne)

            val userTwo = User()
            userTwo.firstName = "Manish"
            userTwo.lastName = "Kumar"
            userList.add(userTwo)

            val userThree = User()
            userThree.firstName = "Sumit"
            userThree.lastName = "Kumar"
            userList.add(userThree)

            return userList
        }

        fun getApiUserList(): List<ApiUser> {

            val apiUserList = ArrayList<ApiUser>()

            val apiUserOne = ApiUser()
            apiUserOne.firstName = "Amit"
            apiUserOne.lastName = "Shekhar"
            apiUserList.add(apiUserOne)

            val apiUserTwo = ApiUser()
            apiUserTwo.firstName = "Manish"
            apiUserTwo.lastName = "Kumar"
            apiUserList.add(apiUserTwo)

            val apiUserThree = ApiUser()
            apiUserThree.firstName = "Sumit"
            apiUserThree.lastName = "Kumar"
            apiUserList.add(apiUserThree)

            return apiUserList
        }

        fun convertApiUserListToUserList(apiUserList: List<ApiUser>): List<User> {

            val userList = ArrayList<User>()

            for (apiUser in apiUserList) {
                val user = User()
                user.firstName = apiUser.firstName
                user.lastName = apiUser.lastName
                userList.add(user)
            }

            return userList
        }

        fun convertApiUserListToApiUserList(apiUserList: List<ApiUser>): List<ApiUser> {
            return apiUserList
        }


        fun getUserListWhoLovesCricket(): List<User> {

            val userList = ArrayList<User>()

            val userOne = User()
            userOne.id = 1
            userOne.firstName = "Amit"
            userOne.lastName = "Shekhar"
            userList.add(userOne)

            val userTwo = User()
            userTwo.id = 2
            userTwo.firstName = "Manish"
            userTwo.lastName = "Kumar"
            userList.add(userTwo)

            return userList
        }


        fun getUserListWhoLovesFootball(): List<User> {

            val userList = ArrayList<User>()

            val userOne = User()
            userOne.id = 1
            userOne.firstName = "Amit"
            userOne.lastName = "Shekhar"
            userList.add(userOne)

            val userTwo = User()
            userTwo.id = 3
            userTwo.firstName = "Sumit"
            userTwo.lastName = "Kumar"
            userList.add(userTwo)

            return userList
        }


        fun filterUserWhoLovesBoth(cricketFans: List<User>, footballFans: List<User>): List<User> {
            val userWhoLovesBoth = ArrayList<User>()
            for (cricketFan in cricketFans) {
                for (footballFan in footballFans) {
                    if (cricketFan.id == footballFan.id) {
                        userWhoLovesBoth.add(cricketFan)
                    }
                }
            }
            return userWhoLovesBoth
        }
    }
}