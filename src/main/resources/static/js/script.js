console.log("Script loaded");

// change theme work
let currentTheme = getTheme();
//initial -->

document.addEventListener("DOMContentLoaded", () => {
    changeTheme();
});

//TODO:
function changeTheme() {
    //set to web page

    changePageTheme(currentTheme, "");
    //set the listener to change theme button
    const changeThemeButton = document.querySelector("#theme_change_button");

    if (!changeThemeButton) {
        return;
    }

    changeThemeButton.addEventListener("click", (event) => {
        let oldTheme = currentTheme;
        console.log("change theme button clicked");
        if (currentTheme === "dark") {
            //theme ko light
            currentTheme = "light";
        } else {
            //theme ko dark
            currentTheme = "dark";
        }
        console.log(currentTheme);
        changePageTheme(currentTheme, oldTheme);
    });
}

//set theme to localstorage
function setTheme(theme) {
    localStorage.setItem("theme", theme);
}

//get theme from localstorage
function getTheme() {
    let theme = localStorage.getItem("theme");
    return theme ? theme : "light";
}

//change current page theme
function changePageTheme(theme, oldTheme) {
    const html = document.querySelector("html");

    //localstorage mein update karenge
    setTheme(theme);
    //remove the current theme

    if (oldTheme) {
        html.classList.remove(oldTheme);
    }
    //set the current theme
    html.classList.add(theme);
    html.setAttribute("data-theme", theme);

    // change the text of button
    const themeButtonText = document
        .querySelector("#theme_change_button")
        ?.querySelector("span");

    if (themeButtonText) {
        themeButtonText.textContent = theme == "light" ? "Dark" : "Light";
    }
}

//change page change theme
