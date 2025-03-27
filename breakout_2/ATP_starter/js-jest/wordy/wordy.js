// This is only a SKELETON file for the 'Wordy' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export const answer = (question) => {
    var strArray = question.split(" ");
    var totalValue = 0;
    var currentOperation;
    for (var i = 2; i < strArray.length; i++) {
        if (i % 2 === 0) {
            strArray[i] = strArray[i].replace("?", "");
            return parseInt(strArray[i]);
        } else {
        }
    }
};
