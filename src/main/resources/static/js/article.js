document.addEventListener("DOMContentLoaded", function () {
    addArticleBtnEvent()
    eventForPage()
})

function addArticleBtnEvent() {
    document.getElementById("addBtn").addEventListener('click', function (event) {
        event.preventDefault()
        let href = this.getAttribute("href")
        fetch(href).then(response => response.text()).then(fragment => {
            document.querySelector("#addModal").innerHTML = fragment
        }).then(() => {
            let modal = new bootstrap.Modal(document.querySelector("#addModal"), {})
            modal.show()
            document.getElementById('add_article')
                .addEventListener('submit', event => submitNewUserForm(event))
        })
    })
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
//
//
async function submitNewUserForm(event) {
    event.preventDefault()
    let formData = new FormData(event.target),
        request = new Request(event.target.action, {// в экшене храниться урл
            method: 'POST',
            body: formData,
            enctype: 'multipart/form-data'
        })
    let response = await fetch(request);//в респонсе будет наш тайбл
    let articleTable = await response.text()
    let modal = bootstrap.Modal.getInstance(document.getElementById('addModal'))
    modal.hide()
    document.querySelector(".article_list").innerHTML = articleTable
    eventForPage()
}

async function submitEditUserForm(event) {
    event.preventDefault()
    let formData = new FormData(event.target),
        request = new Request(event.target.action, {// в экшене храниться урл
            method: 'POST',
            body: formData,
            enctype: 'multipart/form-data'
        })
    let response = await fetch(request);//в респонсе будет наш тайбл
    let articleTable = await response.text()
    let modal = bootstrap.Modal.getInstance(document.getElementById('editModal'))
    modal.hide()
    document.querySelector(".article_list").innerHTML = articleTable
    eventForPage()
}
