function handleExitButtonClick() {
    window.location.href = "/products/management/home";
}

function handleSubmit(event) {
    event.preventDefault();

    const form = event.target;

    const productData = {
        name: form.name.value,
        price: parseInt(form.price.value),
        imageUrl: form.imageUrl.value
    };

    fetch("/api/products", {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(productData)
    })
    .then(res => res.json())
    .then(data => {
        if (data.success) {
            window.location.href = "/products/management/home";
        }
        else {
            alert(data.message + "\nHttp Code: "+data.code);
        }
    })
    .catch(error => {
        console.error("에러 발생: ", error);
        alert("에러 발생");
    });
}