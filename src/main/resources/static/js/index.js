const closeBtn = document.querySelector(".close-btn");
const openBtn = document.querySelector(".sidebar-open");
const sidebar = document.querySelector(".sidebar");

closeBtn && closeBtn.addEventListener("click", () => {
  sidebar.classList.add("sidebar-close");
});

openBtn && openBtn.addEventListener("click", () => {
  sidebar.classList.remove("sidebar-close");
});

const viewDetailsBtn = document.querySelector(".view-details");
const contactsRow = document.querySelector(".card-row");

contactsRow && contactsRow.addEventListener("click", (e) => {
  let id = e.target.dataset.index;
  console.log(`contact index : ${id}`);
  fetch(`http://localhost:5000/user/view-contact/${id}`)
    .then((resp) => {
      console.log(resp);
      return resp.json();
    })
    .then((data) => {
      console.log(data);
      document.querySelector(".modal-body").innerHTML = data.description;
    });
});

