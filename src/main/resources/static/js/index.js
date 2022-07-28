import BACKEND_API from "./backend.js";
import fileUploadHandler from "./fileUpload.js";

const closeBtn = document.querySelector(".close-btn");
const openBtn = document.querySelector(".sidebar-open");
const sidebar = document.querySelector(".sidebar");

let contactId;

closeBtn &&
  closeBtn.addEventListener("click", () => {
    sidebar.classList.add("sidebar-close");
  });

openBtn &&
  openBtn.addEventListener("click", () => {
    sidebar.classList.remove("sidebar-close");
  });

const contactsRow = document.querySelector(".card-row");

contactsRow &&
  contactsRow.addEventListener("click", (e) => {
    let id = e.target.dataset.index;
    fetch(`http://localhost:5000/user/view-contact/${id}`)
      .then((resp) => {
        console.log(resp);
        return resp.json();
      })
      .then((data) => {
        console.log(data);
        contactId = data.id;
        document.querySelector(".modal-title").textContent = data.name;
        document.querySelector(".modal-body").innerHTML =
          generateModelBody(data);
        document
          .querySelector(".modal-update")
          .setAttribute("href", `/user/update-contact?id=${data.id}`);
      });
  });

const generateModelBody = (contact) => {
  let { name, userName, designation, email, phone, description } = contact;
  let modelInnerHtml = `
      <h5>Name :</h5>
      <p>${name}</p>
      <h5>User Name :</h5>
      <p>${userName}</p>
      <h5>Designation :</h5>
      <p>${designation}</p>
      <h5>Email :</h5>
      <p>${email}</p>
      <h5>Phone :</h5>
      <p>${phone}</p>
      <div class="modal-details">${description}</div>`;

  return modelInnerHtml;
};

const deleteBtn = document.querySelector(".modal-delete");
deleteBtn &&
  deleteBtn.addEventListener(
    "click",
    (e) => {
      console.log(contactId);
      fetch(`${BACKEND_API}/user/delete-contact/${contactId}`, {
        method: "delete",
        headers: {
          "Content-Type": "application/json",
        },
      })
        .then((resp) => {
          if (!resp.ok) {
            console.error(resp);
          }
          resp.json();
        })
        .then((data) => {
          console.log(data);
          location.reload();
        })
        .catch((e) => console.error(e.message));
    },
    false
  );

const fileInput = document.querySelector(".custom-file-input");

fileInput && fileInput.addEventListener("change", fileUploadHandler, false);
