var mongoose = require('mongoose');
var uniqueValidator = require('mongoose-unique-validator');

// the host:port must match the location where you are running MongoDB
// the "myDatabase" part can be anything you like
mongoose.connect('mongodb://localhost:27017/myDatabase');
mongoose.set('useCreateIndex', true);

var Schema = mongoose.Schema;

var resourceSchema = new Schema({
    name: { type: String, required: true, unique: true, uniqueCaseInsensitive: true },
    website: String,
    phone: { type: String, required: true },
    description: String,
    location: String,
    zipcode: Number,
    latitude: Number,
    longitude: Number
});

// export personSchema as a class called Resource
module.exports = mongoose.model('Resource', resourceSchema);
resourceSchema.plugin(uniqueValidator);
