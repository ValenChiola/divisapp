$(document).ready(function () {
    const folders = document.querySelectorAll('.folder');
    folders.forEach((folder, i) => {
        folder.addEventListener('contextmenu', e => {
            $("#folderModal" + i).modal("show");
            e.preventDefault();
        });
    });
});

const btn = document.querySelector('#btn');
const loadingBtn = document.querySelector('#loading');

btn.addEventListener('click', () => {
    btn.style.display = 'none';
    loadingBtn.style.display = 'block';
});
/*
 document.addEventListener('DOMContentLoaded', (event) => {
 
 function handleDragStart(e) {
 this.style.opacity = '0.4';
 
 dragSrcEl = this;
 
 e.dataTransfer.effectAllowed = 'move';
 e.dataTransfer.setData('text/html', this.innerHTML);
 }
 
 function handleDragEnd(e) {
 this.style.opacity = '1';
 
 items.forEach(function (item) {
 item.classList.remove('over');
 });
 
 }
 
 function handleDragOver(e) {
 if (e.preventDefault) {
 e.preventDefault();
 }
 
 return false;
 }
 
 function handleDragEnter(e) {
 this.classList.add('over');
 }
 
 function handleDragLeave(e) {
 this.classList.remove('over');
 }
 
 function handleDrop(e) {
 e.stopPropagation();
 
 if (dragSrcEl !== this) {
 dragSrcEl.innerHTML = this.innerHTML;
 this.innerHTML = e.dataTransfer.getData('text/html');
 }
 
 return false;
 }
 
 let items = document.querySelectorAll('.box');
 items.forEach(function (item) {
 item.addEventListener('dragstart', handleDragStart, false);
 item.addEventListener('dragover', handleDragOver, false);
 item.addEventListener('dragenter', handleDragEnter, false);
 item.addEventListener('dragleave', handleDragLeave, false);
 item.addEventListener('dragend', handleDragEnd, false);
 item.addEventListener('drop', handleDrop, false);
 });
 });*/

