const { initializeApp } = require( "firebase/app");
const { getFirestore } = require( "firebase/firestore");

const firebaseApp = initializeApp({
  apiKey: "",
  authDomain: "",
  databaseURL: "",
  projectId: "",
  storageBucket: "",
  messagingSenderId: "",
  appId: ""
}, "Web")

exports.firestore = getFirestore(firebaseApp)