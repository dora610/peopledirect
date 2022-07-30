// import BACKEND_API from "./backend";
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
  }, 1500);
};

const generateSearchResult = (searchArr) => {
  let searchContent = searchArr.map((r) => ` <h4>${r}</h4>`).join("");
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
      generateSearchResult(data.map((r) => r.name));
    });
};

const keyUpHandler = (e, searchInput) => {
  console.log(e.key);
  let searchNodes = document.querySelectorAll(".search-result h4");
  let resultArr = [];
  if (!searchNodes) {
    return;
  }
  resultArr = Array.from(searchNodes);

  resultArr[selectedResultIndex] &&
    resultArr[selectedResultIndex].classList.remove("selected");

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
  }
  resultArr[selectedResultIndex] &&
    resultArr[selectedResultIndex].classList.add("selected");
};

export { searchHandler, keyUpHandler };
