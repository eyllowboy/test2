document.addEventListener("DOMContentLoaded", function (event) {
    eventForPage();
    changeNow();
});
function searchOnArticle() {
    let searchText = document.querySelector("#search_table").value.trim();
    const param = new URLSearchParams({
        "filterText": searchText,
    });
    fetch("allarticles/filter?" + param).then(response => response.text()).then(fragment => {
                    document.querySelector(".article_list").innerHTML = fragment
            eventForUserPage()
        }
    )
}
function eventForPage() {


    document.querySelectorAll('.card .editArticle').forEach(editBtn => editBtn.addEventListener('click', function (event) {
        event.preventDefault()
        let href = this.getAttribute("href")
        fetch(href).then(response => response.text()).then(fragment => {
            document.querySelector("#editModal").innerHTML = fragment
        }).then(() => {
            let modal = new bootstrap.Modal(document.querySelector("#editModal"), {})
            modal.show()
            document.getElementById('edit_article')
                .addEventListener('submit', event => submitEditUserForm(event))
        })
    }))

    document.querySelectorAll('.card .deleteBtn').forEach(deleteBtn => deleteBtn.addEventListener('click', function (event) {
        event.preventDefault()
        let href = this.getAttribute("href")
        document.querySelector('#deleteModal .modal-footer a').setAttribute('href', href)
        let modal = new bootstrap.Modal(document.querySelector("#deleteModal"), {})
        modal.show()
        document.getElementById('delArticle')
            .addEventListener('click', function (event) {
                event.preventDefault()
                fetch(href).then(response => response.text()).then(fragment => {
                    document.querySelector(".article_list").innerHTML = fragment
                    modal.hide()
                    eventForPage()
                })
            })
    }))
}
 



function changeNow() {
    var img = document.getElementById("imgLike");
    if (img.getAttribute("src") === "/img/like.png") {
        img.setAttribute("src", "/img/like1.png");
    } else {
        img.setAttribute("src", "/img/like.png");
    }
}