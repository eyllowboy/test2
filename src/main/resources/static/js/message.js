document.addEventListener("DOMContentLoaded", function (event) {
    eventForUserPage();

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