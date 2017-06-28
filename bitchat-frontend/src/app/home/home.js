angular
    .module('app')
    .service('LoginService', LoginService)
    .component('home', {
        templateUrl: 'app/home/home.html',
        controller: function (BackendUrl, $state, $http, $log, $scope, $rootScope, $stateParams, Notification) {
            var ctrl = this;

            ctrl.logout = function() {

            };


            ctrl.addContact = function(contact) {

                $http.get(BackendUrl+"/rest/user/addcontact/"+contact.login).then(function(response) {
                        var msgContainer = document.getElementById('msgContainer');
                        var div = document.createElement('div');
                        div.setAttribute('class', 'alert alert-success');
                        var textnode = document.createTextNode("Contato " + contact.contactLogin + " adicionado com sucesso!");
                        div.appendChild(textnode);
                        msgContainer.appendChild(div);
                        delete $scope.contact;
                        $scope.listContacts();

                        Notification.success('Success notification');
                    },

                    function (response) {
                        console.log("Error: response from submitting data to server was: " + response);

                        Notification.success('Success notification');
                    });

            };

            var websocket = new WebSocket("ws://localhost:8080/bitchat/chat");

            ctrl.openChat = function(idUserContact, contactLogin) {

                websocket.onopen = function() {
                    var message = { userContactId: idUserContact, messageType: 'OPEN', message: 'teste', sender: $stateParams.login, receiver: '' };
                    websocket.send(JSON.stringify(message));
                };

                websocket.onmessage = function(e) {
                    var textAreaMessage = document.getElementById('message');
                    var msgContainer = document.getElementById('msgContainer');
                    if (msgContainer.childNodes.length == 100) {
                        msgContainer.removeChild(msgContainer.childNodes[0]);
                    }

                    var div = document.createElement('div');
                    var textnode = document.createTextNode(e.data);
                    div.appendChild(textnode);
                    msgContainer.appendChild(div);

                    msgContainer.scrollTop = msgContainer.scrollHeight;
                    textAreaMessage.value = '';
                };

                websocket.onerror = function(e) {};
                websocket.onclose = function(e) {

                };

                var message = { userContactId: idUserContact, messageType: 'OPEN', message: 'teste', sender: $stateParams.login, receiver:'' };
                websocket.send(JSON.stringify(message));

                $scope.idSessao = idUserContact;
                $scope.contactLogin = contactLogin;
                $state.go('chat', {login: $stateParams.login, idSessao: idUserContact, contactLogin: contactLogin});
            };


            ctrl.sendMessage = function(idUserContact) {
                var message = {  userContactId: $stateParams.idSessao, messageType: 'MESSAGE', message: $scope.chatMessage,
                    sender: $stateParams.login, receiver: $stateParams.contactLogin };

                // Send a message through the web-socket
                websocket.send(JSON.stringify(message));
            };


        }
    });


function LoginService($http) {

}