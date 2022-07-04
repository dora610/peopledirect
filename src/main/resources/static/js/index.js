const closeBtn = document.querySelector(".close-btn");
const openBtn = document.querySelector(".sidebar-open");
const sidebar = document.querySelector('.sidebar')

closeBtn.addEventListener('click', ()=>{
    sidebar.classList.add('sidebar-close')
})


openBtn.addEventListener('click', ()=>{
    sidebar.classList.remove('sidebar-close')

})