angular
    .module('app')
    .service('LoginService', LoginService)
    .component('home', {
        templateUrl: 'app/home/home.html',
        controller: function (BackendUrl, WebsocketUrl, $state, $http, $log, $scope, $rootScope, $stateParams, Notification) {
            var ctrl = this;

            ctrl.logout = function() {
                $rootScope.user =  undefined;
                $state.go('login');
            };


            ctrl.addContact = function(contact) {
                
                $http.get(BackendUrl+"/rest/user/"+contact.login).then(function(response) {
                        var c = {}
                        c.contact = response.data;
                        c.user  = $rootScope.user;

                        $http.post(BackendUrl+"/rest/user/addContact/", c).then(function(response) {

                                Notification.success('Contact added');
                            },

                            function (response) {

                                Notification.error('Error in add contact');
                            });

                    },

                    function (response) {

                        Notification.error('Error in add contact');
                    });

            };

            var websocket = new WebSocket(WebsocketUrl+"/chat");

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