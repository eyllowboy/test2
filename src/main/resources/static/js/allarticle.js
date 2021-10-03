document.addEventListener("DOMContentLoaded", function (event) {
    eventForPage();

});
function searchOnArticle() {
    let searchText = document.querySelector("#search_table").value.trim();
    const param = new URLSearchParams({
        "filterText": searchText,
    });
    fetch("allarticles/filter?" + param).then(response => response.text()).then(fragment => {
                    document.querySelector(".article_list").innerHTML = fragment
            eventForPage()
        }
    )
}
function AllArticlesToday(){
    fetch("allarticles/today").then(response => response.text()).then(fragment => {
            document.querySelector(".article_list").innerHTML = fragment
            eventForPage()
        }
    )
}
function eventForPage() {

    const buttons= document.querySelectorAll('.btnKoment');
    buttons.forEach(button=>{button.addEventListener('click',function (event) {
        const currentB=event.currentTarget;
        const hidden =currentB.closest(".card").querySelector(".Komment");
        hidden.classList.toggle("visually-hidden");

    })})
    // const butlike= document.querySelectorAll('.Imglikes');
    // butlike.forEach(button=>{button.addEventListener('click',function (event) {
    //     const currentB=event.currentTarget;
    //     const like =currentB.closest(".mylike").querySelector(".like_");
    //     if (like.getAttribute("src") === "/img/like.jpg") {
    //         like.setAttribute("src", "/img/like1.jpg");
    //     } else {
    //         like.setAttribute("src", "/img/like.jpg");
    //     }
    //
    // })})

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
    document.querySelectorAll('.card .addKoment')
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

function changeNow( pid) {
    console.log(pid)
    let img = document.getElementById("like_"+pid).firstElementChild;
    console.log(img)
    console.log(img.getAttribute("src"))
    if (img.getAttribute("src") === "/img/like.jpg") {
        img.setAttribute("src", "/img/like1.jpg");
        let param = {
            pid : pid
        }
        console.log('+')
        let url = new URL("http://localhost:8060/allarticles/like")
        url.search = new URLSearchParams(param).toString()
        fetch(url).then(response => response.text()).then(fragment => {
                document.querySelector(".article_list").innerHTML = fragment
                eventForPage()
            }
        )

    } else {
        img.setAttribute("src", "/img/like.jpg");
        let param = {
            pid : pid
        }
        console.log('-')
        let url = new URL("http://localhost:8060/allarticles/dislike")
        url.search = new URLSearchParams(param).toString()
        fetch(url).then(response => response.text()).then(fragment => {
                document.querySelector(".article_list").innerHTML = fragment
                eventForPage()
            }
        )
    }


}




