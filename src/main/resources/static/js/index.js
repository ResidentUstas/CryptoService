window.addEventListener('mousemove', function (e) {
    let bg = document.querySelector('.mouse-parallax-bg');
    let input = document.querySelector('.double-border-button');
    let x = e.clientX / window.innerWidth;
    let y = e.clientY / window.innerHeight;
    bg.style.transform = 'translate(-' + x * 50 + 'px, -' + y * 50 + 'px)';

    input.addEventListener('change', function (e) {
        debugger;
        let input_click = document.querySelector('.Submit');
        input_click.click();
    });
});