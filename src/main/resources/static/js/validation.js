function registerValidation() {
    var fName = document.getElementById("fName");
    var lName = document.getElementById("lName");
    var email = document.getElementById("eMail");
    var username = document.getElementById("username");
    var password = document.getElementById("password");

    return (validateUserDetails(fName, lName, email) && validateUsernamePassword(username, password)) === true;
}

function loginValidation() {
    var username = document.getElementById("username");
    var password = document.getElementById("password");

    return validateUsernamePassword(username, password) === true;
}

function validateUsernamePassword(username, password) {
    if(username.value === "" || username.value === " ") {
        alert("Bad username!");
        return false;
    }
    if(password.value === "") {
        alert("Password is blank!");
        return false;
    }
    if(password.length < 8) {
        alert("Password length was " + password.length + " which it must be at least 8 characters!");
        return false;
    }

    return true;
}

function validateUserDetails(fName, lName, eMail) {
    if(fName.length < 3) {
        alert("First name must not be less than 3 characters!");
        return false;
    }
    if(lName.length < 3) {
        alert("First name must not be less than 3 characters and must contain a combination of alphanumeric characters!");
        return false;
    }
    if((!eMail) || eMail.length < 1 || eMail.type !== 'email' && eMail.checkValidity() === false) {
        alert("Not a valid email or was blank!");
        return false;
    }

    return true;
}