document.addEventListener("DOMContentLoaded", function (event) {
    eventForUserPage();
    addMessageBtnEvent();
});
function eventForUserPage() {
    document.querySelectorAll('.table .openBtn')
        .forEach(openBtn => openEvent(openBtn));



    document.querySelectorAll('.table .deleteBtn')
        .forEach(deleteBtn => deleteBtn.addEventListener('click', function (event) {
            event.preventDefault();
            let href = this.getAttribute('href');
            document.querySelector('#deleteModalMessage .modal-footer a').setAttribute('href', href)
            let model = new bootstrap.Modal(document.getElementById('deleteModalMessage'), {});
            model.show();
            let delMessage = document.getElementById('delMessage');
            delMessage.addEventListener('click', ev => deleteMessage(ev))
        }));



}
async function deleteMessage(event) {
    event.preventDefault();
    let el = event.target;
    let href = el.getAttribute('href');
    fetch(href).then(response => response.text()).then(fragment => {
        let modal = bootstrap.Modal.getInstance(document.getElementById('deleteModalMessage'))
        modal.hide();
        document.querySelector(".message_list").innerHTML = fragment;
        eventForUserPage();
    })
}
function openEvent(el) {
    el.addEventListener('click', function (event) {
        event.preventDefault()
        let href = this.getAttribute('href')
        openMessageAsyncFetch(href)
    })
}
function openMessageAsyncFetch(href) {
    fetch(href).then(response => response.text()).then(fragment => {
        document.querySelector("#openModal").innerHTML = fragment;
    }).then(() => {
        let model = new bootstrap.Modal(document.getElementById('openModal'), {});
        model.show();

    });
}
function addMessageBtnEvent() {
    document.getElementById("addBtn").addEventListener('click', function (event) {
        event.preventDefault()
        let href = this.getAttribute("href")
        fetch(href).then(response => response.text()).then(fragment => {
            document.querySelector("#addModal").innerHTML = fragment
        }).then(() => {
            let modal = new bootstrap.Modal(document.querySelector("#addModal"), {})
            modal.show()
            document.getElementById('add_message')
                .addEventListener('submit', event => submitNewMessage(event))
        })
    })
}
async function submitNewMessage(event) {
    event.preventDefault()
    let formData = new FormData(event.target),
        request = new Request(event.target.action, {// в экшене храниться урл
            method: 'POST',
            body: formData,

        })
    let response = await fetch(request);//в респонсе будет наш тайбл
    let messageTable = await response.text()
    let modal = bootstrap.Modal.getInstance(document.getElementById('addModal'))
    modal.hide()
    document.querySelector(".message_list").innerHTML = messageTable
    eventForPage()
}