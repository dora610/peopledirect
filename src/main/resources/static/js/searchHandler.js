import BACKEND_API from "./backend.js";
// import { generateModelBody } from "./viewContact.js";
const searchResultEle = document.querySelector(".search-result");

let controller;
let timeoutRef;
let selectedResultIndex = -1;

const searchHandler = (e) => {
  let searchStr = e.target.value;
  console.log(`Search string: ${searchStr}`);
  if (!searchStr) {
    return;
  }

  if (controller) {
    controller.abort();
  }
  clearTimeout(timeoutRef);
  searchResultEle.innerHTML = "";
  searchResultEle.classList.remove("active");

  timeoutRef = setTimeout(() => {
    searchApiHandler(searchStr);
  }, 1000);
};

const generateSearchResult = (searchResult) => {
  let searchContent = Object.entries(searchResult)
    .map(
      ([id, cName]) =>
        `<button type="button" class="list-group-item list-group-item-action view-details" data-index=${id} data-bs-toggle="modal" data-bs-target="#contactDetailsModal">${cName}</button>`
    )
    .join("");
  searchResultEle.insertAdjacentHTML("beforeend", searchContent);
  searchResultEle.classList.add("active");
};

const searchApiHandler = (searchStr) => {
  controller = new AbortController();
  searchResultEle.textContent = "Loading...";
  searchResultEle.classList.add("active");

  fetch(`http://localhost:5000/user/api/contact?q=${searchStr}`, {
    headers: {
      "Content-Type": "application/json",
    },
    signal: controller.signal,
  })
    .then((resp) => {
      console.log(resp);
      return resp.json();
    })
    .then((data) => {
      console.log(data);
      searchResultEle.innerHTML = "";
      generateSearchResult(data);
    });
};

const keyUpHandler = (e, searchInput) => {
  console.log(e.key);
  let searchNodes = document.querySelectorAll(".search-result button");
  let resultArr = [];
  if (!searchNodes) {
    return;
  }
  resultArr = Array.from(searchNodes);

  resultArr[selectedResultIndex] &&
    resultArr[selectedResultIndex].classList.remove("active");

  if (e.key === "Escape") {
    clearTimeout(timeoutRef);
    controller && controller.abort();
    searchResultEle.innerHTML = "";
    searchResultEle.classList.remove("active");
    console.log(searchInput);
    searchInput.value = "";
  } else if (e.key === "ArrowDown" || e.key === "ArrowRight") {
    if (
      selectedResultIndex < 0 ||
      selectedResultIndex === resultArr.length - 1
    ) {
      selectedResultIndex = 0;
    } else {
      selectedResultIndex++;
    }
  } else if (e.key === "ArrowUp" || e.key === "ArrowLeft") {
    if (selectedResultIndex <= 0) {
      selectedResultIndex = resultArr.length - 1;
    } else {
      selectedResultIndex--;
    }
  } else if(e.key === "Enter"){
    if(selectedResultIndex >= 0 && selectedResultIndex < resultArr.length){
      let id = searchNodes[selectedResultIndex].dataset.index;
      console.log(id);
      showContactDetails(id);
    }
  }
  resultArr[selectedResultIndex] &&
    resultArr[selectedResultIndex].classList.add("active");
};


const showContactDetails = (id) => {
  fetch(`${BACKEND_API}/user/view-contact/${id}`,{
    headers:{
      "Content-Type": "application/json"
    }
  })
    .then((resp) => resp.json())
    .then((data) => {
      document.querySelector(".modal-title").textContent = data.name;
      console.log(document.querySelector(".modal-body"));
      document.querySelector(".modal-body").innerHTML = generateModelBody(data);
      document
        .querySelector(".modal-update")
        .setAttribute("href", `/user/update-contact?id=${data.id}`);
    })
    .catch((err) => console.error(err.message));
};

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

export { searchHandler, keyUpHandler };
