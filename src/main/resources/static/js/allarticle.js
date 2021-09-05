document.addEventListener("DOMContentLoaded", function (event) {
    eventForUserPage();

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