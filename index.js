const express=require("express");
const { async } = require("@firebase/util");
const { displayData, addData } = require("./dataQuery")

const app = express()
app.use(express.json());

app.get("/",(req,res) =>{
    res.send("d : display\na : add single data\nam : add multiple data")
})

app.get("/d",async (req,res) => {
    console.log(req.query.subject)
    const result = await displayData(req.query.subject);
    res.send(result)
})

app.post("/a", async (req,res) => {
    const result = await addData(req.body);
    res.send(result)
})

app.get("/da", async (req,res) => {
    res.send({
        "Text": "What will be the output of the following C code?\n#include <stdio.h>\nint main()\n{\n    int y = 10000;\n    int y = 34;\n    printf(\"Hello World! %d\\n\", y);\n    return 0;\n}",
        "CorrectAnswer": "Compile time error",
        "Options": ["Compile time error", "Hello World! 34", "Hello World! 1000", "Hello World! followed by a junk value"]
    })
})

app.listen(3000,() => {console.log("running...")})