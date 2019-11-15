var express = require('express');
var router = express.Router();
let twilio = require('twilio');

// TODO: change to environment variables
const accountSid = '';
const authToken = '';
const client = require('twilio')(accountSid, authToken);

// TODO: subcription start: msg from the user "START"
// TODO: After enrolling msg: Welcome to the study
// TODO: starting question just after the enrolling: "Please indicate your symptom (1)Headache, (2)Dizziness, (3)Nausea, (4)Fatigue, (5)Sadness, (0)None"
// TODO: if invalid then send "Please enter a number from 0 to 5"\
// TODO: if selected 0 then send "Thank you and we will check with you later." and stop sending data to user
// TODO: then send msg: "On a scale from 0 (none) to 4 (severe), how would you rate your "xxxx" in the last 24 hours?"
// TODO: if invalid msg then send: "Please enter a number from 0 to 4"
// TODO: followup  message 
          // if 1 or 2 : then send "You have a mild xxxx" where xxxx is the symptom.
          // if 3 : then send "You have a moderate xxxx" where xxxx is the symptom.
          // if 4 : then send "You have a severe xxxx" where xxxx is the symptom.
          // if 0 : then send "You do not have a xxxx"
  // TODO: repeate the step one and hence the loop for 3 times and removing the selected element at each step
  
/* GET home page. */
router.get('/', function(req, res, next) {
  client.messages
  .create({
     body: 'This is the ship that made the Kessel Run in fourteen parsecs?',
     // TODO: change to your number
     from: '+12563336766',
     to: '+19803131561'
   })
  .then(message => console.log(message.sid));
  res.render('index', { title: 'Express' });
});




module.exports = router;
