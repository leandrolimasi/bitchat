angular
    .module('app')
    .service('LoginService', LoginService)
    .component('home', {
        templateUrl: 'app/home/home.html',
        controller: function (LoginService) {
            var ctrl = this;

            ctrl.login = function(hero, prop, value) {
                hero[prop] = value;


                $http.post("http://localhost:8080/bitchat/rest/user/login", user).then(function(response) {
                        console.log("Successful: response from submitting data to server was: " + response.data.login);
                        if(response.data  != '' && response.data !== null && response.data !== undefined){
                            $state.go('home', {login: user.login});
                            delete $scope.user;
                        } else {
                            var msgContainer = document.getElementById('msgContainer');

                            var div = document.createElement('div');
                            div.setAttribute('class', 'alert alert-danger');
                            var textnode = document.createTextNode('Usuário e/ou senha inválidos!');
                            div.appendChild(textnode);
                            msgContainer.appendChild(div);
                        }
                    },

                    function (response) {
                        console.log("Error: response from submitting data to server was: " + response.data);

                        //USING THE PROMISE REJECT FUNC TO CATCH ERRORS
                        deferred.reject({
                            data: response //RETURNING RESPONSE SINCE `DATA` IS NOT DEFINED
                        });


                    });

            };
        }
    });


function LoginService($http) {

}