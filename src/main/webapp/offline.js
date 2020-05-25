window.addEventListener('load', () => {
    getUsername();
});

function getUsername() {
    document.getElementById("username_ph").innerHTML
        = localStorage.getItem("username");

}