import fileUploadHandler from "./fileUpload.js";
import viewContactHandler from "./viewContact.js";
import deleteContactHandler from "./deleteContact.js";
import { searchHandler, keyUpHandler } from "./searchHandler.js";

const closeBtn = document.querySelector(".close-btn");
const openBtn = document.querySelector(".sidebar-open");
const sidebar = document.querySelector(".sidebar");

closeBtn &&
  closeBtn.addEventListener("click", () => {
    sidebar.classList.add("sidebar-close");
  });

openBtn &&
  openBtn.addEventListener("click", () => {
    sidebar.classList.remove("sidebar-close");
  });

const contactsRow = document.querySelector(".card-row");
contactsRow && contactsRow.addEventListener("click", viewContactHandler);

const deleteBtn = document.querySelector(".modal-delete");
deleteBtn && deleteBtn.addEventListener("click", deleteContactHandler, false);

const fileInput = document.querySelector(".custom-file-input");
fileInput && fileInput.addEventListener("change", fileUploadHandler, false);

const searchInput = document.querySelector(".search-bar input");
searchInput && searchInput.addEventListener("change", searchHandler);

const searchBar = document.querySelector(".search-bar");
searchBar &&
  searchBar.addEventListener("keyup", e=>keyUpHandler(e, searchInput));
