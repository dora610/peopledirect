import BACKEND_API from "./backend.js";

const deleteContactHandler = (e) => {
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
    .then((data) => location.reload())
    .catch((err) => console.error(err.message));
};

export default deleteContactHandler;
