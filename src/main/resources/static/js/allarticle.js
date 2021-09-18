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
    document.body.addEventListener("click", (e) => {
        const button = e.target.closest(".btnKoment");
        if (button) {
            const hidden = button.closest(".card").querySelector(".Komment");
            hidden.classList.toggle("visually-hidden");
        }

    })



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
    document.querySelectorAll('.card .Koment')
        .forEach(Koment => messageEvent(Koment));
}

function messageEvent(el) {
    el.addEventListener('click', function (event) {
        event.preventDefault()
        let href = this.getAttribute('href')
        messageAsyncFetch(href)
    })
}
function messageAsyncFetch(href) {

    fetch(href).then(response => response.text()).then(fragment => {
        document.querySelector("#commentModal").innerHTML = fragment;

    }).then(() => {
        let model = new bootstrap.Modal(document.getElementById('commentModal'), {});
        model.show();
        document.getElementById("comment_user")
            .addEventListener('submit', event => submitMessageUserForm(event))
    });
}
async function submitMessageUserForm(event) {
    event.preventDefault();
    let formData = new FormData(event.target),
        request = new Request(event.target.action, {
            method: 'POST',
            body: formData
        });
    let response = await fetch(request);
    let userTable = await response.text();
    console.dir(userTable)

    let modal = bootstrap.Modal.getInstance(document.getElementById('commentModal'))
    modal.hide();
    document.querySelector(".article_list").innerHTML = userTable;
    eventForPage();
}

function changeNow() {
    var img = document.getElementById("imgLike");
    if (img.getAttribute("src") === "/img/like.png") {
        img.setAttribute("src", "/img/like1.png");
    } else {
        img.setAttribute("src", "/img/like.png");
    }
}




