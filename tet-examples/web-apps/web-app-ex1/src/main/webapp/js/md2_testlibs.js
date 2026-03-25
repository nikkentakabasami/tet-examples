

function testMoment(){

	let m1 = moment("2013-02-08");
	let m1f = m1.format('DD.MM.YYYY, hh:mm:ss');
	$("#infopanel").text(`${String(testMoment)}
	${m1f}`);
		
	console.log('format:',m1f);
}


$(()=>{
	
	testMoment();
	
	
});


