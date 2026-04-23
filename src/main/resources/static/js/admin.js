console.log("Admin user")

document.querySelector("#image_file_input").addEventListener("change", (e) => {
    let file = e.target.files[0];
    let reader = new FileReader();
    reader.onloadend = function () {
        document.getElementById("upload_image_preview")
            .setAttribute("src", reader.result);
    }
    reader.readAsDataURL(file);
})