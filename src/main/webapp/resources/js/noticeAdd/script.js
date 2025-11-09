document.getElementById("buttonSubmit").addEventListener("click", function() {
	// 객체
	const formData = {
		memID:document.getElementById("memID").value,
		title:document.getElementById("title").value,
		content:document.getElementById("content").value,
		writer:document.getElementById("writer").value,
	}

// index.jsp 파일에서 만든 메타 CSRF 태그 두개를 js 파일로 가져오기
const csrfToken = document.querySelector("meta[name='_csrf']").getAttribute("content");
const csrfHeader = document.querySelector("meta[name='_csrf_header']").getAttribute("content");

 fetch("/menu/add", {
	method:"POST",
	headers:{
		'Content-Type':'application/json',
		[csrfHeader]:csrfToken // CSRF 헤더와 토큰을 동적으로 추가
	},
	body:JSON.stringify(formData)
 }).then(response => {
	if (!response.ok) {
		throw new Error("공지사항 작성 실패.")
	}
	return response.text(); // => "게시글 잘 작성됨"
 }).then(_ => {
	console.log("Success");
	window.location.href="/"; // localhost:8080으로 페이지 이
 }).catch(error => {
	console.log("Error가 발생", error);
 })
 });