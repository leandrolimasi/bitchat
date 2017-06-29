angular
    .module('app')
    .component('chat', {
        templateUrl: 'app/chat/chat.html',
        controller: function (WebsocketUrl, BackendUrl, $state, $http, $log, $scope, $rootScope, $stateParams, $q) {
            var ctrl = this;
            ctrl.websocket;
            ctrl.room;
            ctrl.chats = [];


            ctrl.openChat = function(login, contactLogin) {

                var promises = [];
                promises.push($http.get(BackendUrl+"/rest/user/"+contactLogin));
                promises.push($http.get(BackendUrl+"/rest/user/"+login));

                $q.all(promises).then(function(results){
                    ctrl.contact = results[0].data;
                    ctrl.user = results[1].data;
                    ctrl.room = ctrl.user.login +'-'+ctrl.contact.login;
                    ctrl.websocket = new WebSocket(WebsocketUrl+"/chat/"+ctrl.room);

                    ctrl.websocket.onopen = function(e) {
                        $log.info(e);
                    };

                    ctrl.websocket.onmessage = function(e) {
                        ctrl.chats.push(JSON.parse(e.data));
                    };

                    ctrl.websocket.onerror = function(e) {};
                    ctrl.websocket.onclose = function(e) {};

                });

            };

            ctrl.sendMessage = function() {
                var msg = '{"content":"' + ctrl.message + '", "sender":"'
                    + ctrl.user.login + '", "received":""}';
                ctrl.websocket.send(msg);
                ctrl.message= undefined;
            };
            
            ctrl.openChat($stateParams.login, $stateParams.contactLogin);

        }
    });
