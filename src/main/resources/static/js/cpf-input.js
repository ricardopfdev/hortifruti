(function () {
    function formatCpf(digits) {
        digits = digits.slice(0, 11);
        if (digits.length <= 3) {
            return digits;
        }
        if (digits.length <= 6) {
            return digits.slice(0, 3) + '.' + digits.slice(3);
        }
        if (digits.length <= 9) {
            return digits.slice(0, 3) + '.' + digits.slice(3, 6) + '.' + digits.slice(6);
        }
        return digits.slice(0, 3) + '.' + digits.slice(3, 6) + '.' + digits.slice(6, 9) + '-' + digits.slice(9);
    }

    function applyCpfMask(input) {
        var digits = input.value.replace(/\D/g, '').slice(0, 11);
        input.value = formatCpf(digits);
    }

    function bind(input) {
        input.addEventListener('input', function () {
            applyCpfMask(input);
        });
        input.addEventListener('paste', function (event) {
            event.preventDefault();
            var pasted = (event.clipboardData || window.clipboardData).getData('text') || '';
            input.value = pasted;
            applyCpfMask(input);
        });
        applyCpfMask(input);
    }

    document.querySelectorAll('[data-cpf-input]').forEach(bind);
})();
