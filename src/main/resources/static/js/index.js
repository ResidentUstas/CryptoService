window.addEventListener('mousemove', function (e) {
    let bg = document.querySelector('.mouse-parallax-bg');
    let input = document.querySelector('.input_file');
    let input_click = document.querySelector('.input_click');
    let x = e.clientX / window.innerWidth;
    let y = e.clientY / window.innerHeight;
    bg.style.transform = 'translate(-' + x * 50 + 'px, -' + y * 50 + 'px)';

    input.addEventListener('change', function (e) {
        let countFiles = '';
        let label = input.nextElementSibling;
        let labelVal = label.innerText;
        if (this.files && this.files.length >= 1)
            countFiles = this.files.length;

        if (countFiles)
            if (countFiles == 1)
                label.innerText = 'Файл выбран!';
            else
                label.innerText = 'Выбрано файлов ' + countFiles;
        else
            label.innerText = labelVal;
    });
});