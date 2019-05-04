<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
</head>
<body>

<input type="file" id="input1">
<%--<input type="file" id="input1">--%>
<input type="file" id="input2" multiple/>
<button id="sendbtn">Send</button>
<%--<div id = "div0">--%>
<div id = "div1">
    <div id = "div2">

</div>
<%--<script type="text/javascript">--%>
    <%--function getfolder(e) {--%>
        <%--var files = e.target.files;--%>
        <%--for (var i = 0; i < files.length; ++i) {--%>
            <%--// console.log(files[i].name);--%>
            <%--console.log(files[i].webkitRelativePath);--%>
        <%--}--%>
        <%--var path = files[0].webkitRelativePath;--%>
        <%--var Folder = path.split("/");--%>
        <%--alert(Folder[0]);--%>
    <%--}--%>
<%--</script>--%>

<%--<input type="file" id="flup" onchange="getfolder(event)" webkitdirectory mozdirectory msdirectory odirectory directory multiple />--%>

<script>

    var fileContent1;
    var fileExtension1;
    var fileContentMult = [];
    var fileExtensionMult = [];
    var data;

    input1.onchange = e => {

        var selectedFile = document.getElementById('input1').files[0];
        var reader = new FileReader();
        reader.readAsText(selectedFile,'UTF-8');
        var fileName = selectedFile.name;

        reader.onload = readerEvent => {
            fileContent1 = readerEvent.target.result; // this is the content!
            fileExtension1 = fileName.split('.').pop();
        }
    }

    input2.onchange = e => {

        var selectedFile = document.getElementById('input2').files;
        var reader = [];
        var fileNames = [];
        for (var i = 0; i < selectedFile.length; ++i){
            var tmp = new FileReader();
            reader.push(tmp);
            reader[i].readAsText(selectedFile[i],'UTF-8');
            fileNames.push(selectedFile[i].name);
            // console.log(fileNames[i]);
            reader[i].onload = (function(j) {

                return function (ev) {
                    // console.log(j);
                    fileContentMult.push(ev.target.result); // this is the content!
                    // console.log(fileNames[j])
                    fileExtensionMult.push(fileNames[j].split('.').pop());
                }
            })(i);
        }

    }

    $("#sendbtn").click( () => {
        console.log(fileContent1);
    console.log(fileContentMult[0]);
        $.ajax({
        type : "POST",
        url : "/send",
        data : JSON.stringify({
            "code1" : fileContent1,
            "ext1" : fileExtension1,
            "code2" : fileContentMult,
            "ext2": fileExtensionMult
        }),
        dataType : "json",
        contentType: "application/json;charset=UTF-8",
            success : (response) => {
                console.log(response);
                var divMsg1 = document.getElementById("div1");
                var size = response.test.length;
                var res = JSON.stringify(response.test[0]).split('",');
                var plag = JSON.stringify(response.plag[0]).split('",');
                console.log(response.coef);
                var coef = JSON.stringify(response.coef[0]).split(',');
                // var ttmp = response.coef[0][1];
                // console.log(ttmp);
                // JSON.
                // var coeff = response.coef;

                var divMsg0 = document.getElementById("div0");
                // divMsg1.innerHTML += "<center> <h1> " + JSON.stringify(response.coef[0]) + "</h1></center>";
                // divMsg0.innerHTML += "<h1>" + coeff + "</h1>";

                var k = 0;
                for (var i = 0; i < res.length; ++i) {

                    var tmp = res[i];
                    var tmp2 = "";
                    var tmpPlag = plag[i];
                    var tmpP = "";
                    var x = 1;
                    var y = 0;
                    if (i == 0) x = 2;
                    if (i + 1 == res.length) y = 1;
                    for (var j = x; j + y < tmp.length; ++j) {
                        tmp2 += tmp[j];
                    }
                    for (var j = x; j + y < tmpPlag.length; ++j) {
                        tmpP += tmpPlag[j];
                    }


                    if (tmp2 == "lol" && tmpP == "kek" || i + 1 == res.length) {
                        divMsg1.innerHTML += "<center> <h1> " + response.coef[0][k] + "</h1></center>";
                        ++k;
                        divMsg1.innerHTML += "<p><h2>------------------------------------------</h2></p>";
                    } else {
                        divMsg1.innerHTML += "<p style=\"color:green\">" + tmp2 + "</p>";
                        divMsg1.innerHTML += "<p style=\"color:red\">" + tmpP + "</p>";
                        divMsg1.innerHTML += "<p>----------------------------------------</p>";
                    }

                    // console.log(response.test[i]);
                }
                // divMsg1.innerHTML = JSON.stringify(response.test).toString();


            },
            error : (error) => {
                console.log(error)
            }
        })
    });
</script>
</body>
</html>
