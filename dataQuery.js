const { async } = require("@firebase/util");
const { firestore } = require("./config");
const { collection, getDocs, setDoc, doc} = require("firebase/firestore");

exports.displayData = async (subject,i = 0) => {
    try {
        let path = "Subjects/" + subject + "/Questions";
        const coll = collection(firestore, path);
        const snap = await getDocs(coll);
        const data = snap.docs.map((doc) => doc.data());
        if ( i == 1) {
            return data.length
        }
        return data;
    } catch (error) {
        return error;
    }
};

exports.addData = async (question) => {
    try {
        if (question.length === 0) {
            throw ("No questions provided");
        }

        let subject = question[0].subject
        const len = await this.displayData(subject,1);
        let path = "Subjects/" + subject + "/Questions";
        const coll = collection(firestore, path);
        
        for (let i = 1;i < question.length;i++) {
            const docId = "Question"+(len+i);
            try {
                const res = await setDoc(doc(firestore,path,docId),question[i],{ merge:true });
                console.log(`Document added: ${docId}`);
            } catch (error) {
              console.log(error)
            }
        }
    
    } catch (error) {
        return error;
    }
};
