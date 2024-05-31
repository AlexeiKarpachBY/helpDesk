import React from "react";
import {Button, TextField, Typography} from "@material-ui/core";


class LoginPage extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            nameValue: "",
            passwordValue: "",
            error: []
        };
    }

    handleNameChange = (event) => {
        this.setState({
            nameValue: event.target.value
        });
    };

    handlePasswordChange = (event) => {
        this.setState({
            passwordValue: event.target.value
        });
    };

    handleClickAuth = async () => {
        await this.setState({
            error: []
        })
        if (this.state.nameValue.length > 0 && this.state.passwordValue.length > 0) {
            let user = {
                login: this.state.nameValue,
                password: this.state.passwordValue
            };

            let response = await fetch('http://localhost:8080/auth', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json;charset=utf-8'
                },
                body: JSON.stringify(user)
            }).catch(() => {
                console.log("ERROR")
            })

            let result = await response.json();

            if (response.ok) {
                localStorage.setItem("authorization", result.token);
                this.props.authCallback(true);
            } else {
                this.state.error.push(result.message);
                this.setState({})
            }
        } else {
            this.state.error.push("Please fill out the required field.");
            this.setState({})
        }
    }

    render() {
        const {
            error
        } = this.state;


        return (
            <div className="container">
                <div className="container__title-wrapper">
                    <Typography component="h2" variant="h3" color={"primary"}>
                        Login to the Help Desk
                    </Typography>
                </div>
                <div className="container__from-wrapper">
                    <form>
                        <div className="container__inputs-wrapper">
                            <div className="form__input-wrapper">
                                <TextField
                                    onChange={this.handleNameChange}
                                    label="User name"
                                    variant="outlined"
                                    placeholder="User name"
                                />
                            </div>
                            <div className="form__input-wrapper">
                                <TextField
                                    onChange={this.handlePasswordChange}
                                    label="Password"
                                    variant="outlined"
                                    type="password"
                                    placeholder="Password"
                                />
                            </div>
                        </div>
                        {error.length !== 0 && (
                            <div align={"center"}>
                                <br></br>
                                <Typography display="block" variant="h4" color={"secondary"}> ERROR</Typography>
                                {error.map((el) => {
                                        return (
                                            <Typography color={"secondary"}>
                                                {el}
                                            </Typography>
                                        )
                                    }
                                )}
                            </div>
                        )}
                    </form>
                </div>
                <div className="container__button-wrapper">
                    <Button
                        size="large"
                        variant="contained"
                        color="primary"
                        onClick={this.handleClickAuth}
                    >
                        Enter
                    </Button>
                </div>
            </div>
        );
    }
}

export default LoginPage;
