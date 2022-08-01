import BACKEND_API from "./backend.js";

const viewContactHandler = (e) => {
  let id = e.target.dataset.index;
  fetch(`${BACKEND_API}/user/view-contact/${id}`,{
    headers:{
      "Content-Type": "application/json"
    }
  })
    .then((resp) => {
    //   console.log(resp);
      return resp.json();
    })
    .then((data) => {
      document.querySelector(".modal-title").textContent = data.name;
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

export {viewContactHandler, generateModelBody};
