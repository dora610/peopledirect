const thumbnail = document.querySelector(".thumbnail");
const fileInput = document.querySelector(".custom-file-input");
const FILE_SIZE_LIMIT = 1024 * 1024 * 2;

fileInput && fileInput.addEventListener(
  "change",
  (e) => {
    let file = e.target.files[0];
    if (!file || !file.type.includes("image")) {
      thumbnail.insertAdjacentHTML(
        "afterbegin",
        "<h2>Invalid file selected</h2>"
      );
      return;
    }
    if (file.size > FILE_SIZE_LIMIT) {
      thumbnail.insertAdjacentHTML(
        "afterbegin",
        `<h2>File size - ${calculateFileSize(file.size)} exceeded limit!!</h2>`
      );
      return;
    }

    thumbnail.innerHTML = "";

    let imgEle = document.createElement("img");
    imgEle.src = URL.createObjectURL(file);
    imgEle.height = 100;
    imgEle.classList.add = "thumb-img";
    imgEle.onload = function () {
      URL.revokeObjectURL(imgEle.src);
    };
    thumbnail.appendChild(imgEle);

    thumbnail.insertAdjacentHTML("beforeend", `<p>Filename: ${file.name}</p>`);
    thumbnail.insertAdjacentHTML(
      "beforeend",
      `<p>Filesize: ${calculateFileSize(file.size)}</p>`
    );
  },
  false
);

const calculateFileSize = (sizeInBytes) => {
  const units = ["KB", "MB", "GB"];
  let res = "";
  let unitIndex = 0;
  while (sizeInBytes / 1024 > 1) {
    sizeInBytes = sizeInBytes / 1024;
    res = `${Math.ceil(sizeInBytes)} ${units[unitIndex]} ${res}`;
    unitIndex++;
  }

  return res;
};