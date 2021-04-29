var mongoose = require('mongoose');

// the host:port must match the location where you are running MongoDB
// the "myDatabase" part can be anything you like
mongoose.connect('mongodb://localhost:27017/myDatabase');

var Schema = mongoose.Schema;

var suggestionSchema = new Schema({
    name: {type: String, required: true, unique: true},
    website: String,
    phone: {type: String, required: true},
    description: String
});

// export personSchema as a class called Resource
module.exports = mongoose.model('Suggestion', suggestionSchema);
