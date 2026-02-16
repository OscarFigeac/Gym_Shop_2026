function registerValidation() {
    var fName = document.getElementById("fName");
    var lName = document.getElementById("lName");
    var email = document.getElementById("eMail");
    var username = document.getElementById("reg-username");
    var password = document.getElementById("reg-password");

    return (validateUserDetails(fName, lName, email) && validateUsernamePassword(username, password)) === true;
}

function loginValidation() {
    var username = document.getElementById("username");
    var password = document.getElementById("password");

    return validateUsernamePassword(username, password) === true;
}

function validateUsernamePassword(username, password) {
    if (!username.value.trim()) {
        alert("Username cannot be empty.");
        return false;
    }
    if (password.value.length < 8) {
        alert("Password must be at least 8 characters. (Current length: " + password.value.length + ")");
        return false;
    }
    return true;
}

function validateUserDetails(fName, lName, eMail) {
    if (fName.value.trim().length < 3) {
        alert("First name must be at least 3 characters.");
        return false;
    }
    if (lName.value.trim().length < 3) {
        alert("Last name must be at least 3 characters.");
        return false;
    }

    //using the browser's built in validation
    if (!eMail.checkValidity()) {
        alert("Please enter a valid email address.");
        return false;
    }
    return true;
}

function togglePass(id) {
    const field = document.getElementById(id);
    const toggle = field.parentElement.querySelector('.toggle-password');
    if (field.type === "password") {
        field.type = "text";
        toggle.innerText = "HIDE";
    } else {
        field.type = "password";
        toggle.innerText = "SHOW";
    }
}

(function () {
    'use strict'
    const forms = document.querySelectorAll('.needs-validation')

    Array.prototype.slice.call(forms).forEach(function (form) {
        form.addEventListener('submit', function (event) {
            let isValid = true;
            if (form.id === "registerForm") isValid = registerValidation();
            if (form.id === "loginForm") isValid = loginValidation();

            if (!form.checkValidity() || !isValid) {
                event.preventDefault();
                event.stopPropagation();
            } else {
                const btn = form.querySelector('.btn-gym');
                btn.innerText = "PROCESSING...";
                btn.style.opacity = "0.7";
                btn.style.pointerEvents = "none";
            }
            form.classList.add('was-validated');
        }, false);
    });
})();