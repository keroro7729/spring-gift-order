function handleChangeMode() {
    document.querySelectorAll("#productForm input").forEach(input => {
        input.disabled = false;
    });
    document.getElementById("editButton").style.display = "none";
    document.getElementById("saveButton").style.display = "inline-block";
}

function handleExit() {
    window.location = "/products/management/home";
}

function saveProduct(id) {
    const productData = {
        name: document.getElementById("name").value,
        price: parseInt(document.getElementById("price").value),
        imageUrl: document.getElementById("imageUrl").value
    };

    fetch("/api/products/"+id, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(productData)
    })
    .then(res => res.json())
    .then(data => {
        if (data.success) {
            location.reload();
        }
        else if (data.code == 202) {
            alert(data.message);
            window.location.href = "/products/management/home";
        }
        else {
            alert(data.message);
        }
    })
    .catch(error => {
        console.error("수정 중 에러:", error);
        alert("에러 발생");
    });
}