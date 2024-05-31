import React from "react";
import PropTypes from "prop-types";
import {Redirect, withRouter} from "react-router";
import {
    Button,
    FormControl,
    InputLabel,
    MenuItem,
    Select, TextField,
    Typography
} from "@material-ui/core";
import {FEEDBACK_RATE} from "../constants/inputsValues";
import {Link} from "react-router-dom";

class Feedback extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            rate: 'SATISFACTORILY',
            text: '',
            id: '',
            name: '',
            assignee: "",
            feedbackRate: "",
            feedbackId: null,
            feedbackText: ""
        };
    }


    componentDidMount() {
        this.performRequest()
    }

    performRequest() {
        if (localStorage.getItem('authorization') !== null) {
            const {ticketId} = this.props.match.params;
            fetch('http://localhost:8080/ticket/' + ticketId, {
                method: 'GET',
                headers: {
                    'Authorization': 'Bearer ' + localStorage.getItem('authorization'),
                    'Content-Type': 'application/json;charset=utf-8'
                }
            }).then(response => response.json())
                .then(json => {
                    this.setState({
                        feedbackId: json.feedbackDto.id,
                        id: json.id,
                        name: json.name,
                        assignee: json.assignee,
                        feedbackRate: json.feedbackDto.rate,
                        feedbackText: json.feedbackDto.text,
                    });
                });
        }
    }

    handleRateChange = (event) => {
        this.setState({
            rate: event.target.value
        });
    };

    handleTextChange = (event) => {
        this.setState({
            text: event.target.value
        });
    };

    handleSubmit = async () => {
        const {ticketId} = this.props.match.params;
        const response = await fetch('http://localhost:8080/ticket/feedback/' + ticketId, {
            method: 'POST',
            headers: {
                'Authorization': 'Bearer ' + localStorage.getItem('authorization'),
                'Content-Type': 'application/json;charset=utf-8'
            },
            body: JSON.stringify({
                text: this.state.text,
                rate: this.state.rate

            })
        })

        const result = await response.json();
        if (response.ok) {
            this.performRequest();
        }

    };


    render() {
        const {
            feedbackId,
            id,
            name,
            assignee,
            rate,
            text,
            feedbackRate,
            feedbackText
        } = this.state

        if (localStorage.getItem('authorization') === null) {
            return <Redirect to={`/`}/>;
        }

        return (
            <switch>

                <div className="buttons-container">
                    <Button component={Link} to="/main-page" variant="contained" color={"primary"}>
                        Ticket list
                    </Button>
                    <Button color={"primary"} component={Link} to={`/ticket-info/${id}`} variant="contained">
                        Overview
                    </Button>
                </div>
                <br></br>
                <div align={"center"}>
                    <div>
                        <Typography display="block" variant="h3" color={"primary"}>FEEDBACK</Typography>
                    </div>
                    <div>
                        <br></br>
                    </div>
                    <div>
                        <Typography display="block" variant="h5" color={"primary"}>
                            Ticket name
                        </Typography>
                    </div>
                    <div>
                        <Typography display="block" variant="h3" color={"secondary"}>
                            "{name}"
                        </Typography>
                    </div>
                    <div>
                        <br></br>
                    </div>
                    <div>
                        <Typography display="block" variant="h5" color={"primary"}>
                            Assignee
                        </Typography>
                    </div>
                    <div>
                        <Typography display="block" variant="h3" color={"secondary"}>
                            {assignee}
                        </Typography>
                    </div>
                    <br></br>
                    {feedbackId === null && (
                        <div>
                            <div>
                                <Typography display="block" variant="h5" color={"primary"}>
                                    Choose rate
                                </Typography>
                            </div>
                            <br></br>
                            <div className="inputs-section__ticket-creation-input ticket-creation-input">
                                <FormControl variant="outlined" required>
                                    <InputLabel shrink htmlFor="rate-label">
                                        Rate
                                    </InputLabel>
                                    <Select
                                        value={rate}
                                        label="Rate"
                                        onChange={this.handleRateChange}
                                        className={"ticket-creation-input_width200"}
                                        inputProps={{
                                            name: rate,
                                            id: "rate-label",
                                        }}
                                    >
                                        {FEEDBACK_RATE.map((item, index) => {
                                            return (
                                                <MenuItem value={item.value} key={index}>
                                                    {item.label}
                                                </MenuItem>
                                            );
                                        })}
                                    </Select>
                                </FormControl>
                            </div>

                            <br></br>
                            <br></br>
                            <div align={"center"}>
                                <Typography display="block" variant="h5" color={"primary"}>
                                    Leave your feedback
                                </Typography>
                            </div>
                            <div className="inputs-section">
                                <FormControl>
                                    <TextField
                                        label="Feedback"
                                        multiline
                                        rows={4}
                                        variant="outlined"
                                        value={text}
                                        className="creation-text-field creation-text-field_width680"
                                        onChange={this.handleTextChange}
                                    />
                                </FormControl>
                            </div>
                            <div>
                                <br></br>
                            </div>
                            <div align={"center"}>
                                <Button color={"primary"}
                                        onClick={this.handleSubmit}
                                        variant="contained">
                                    Submit feedback
                                </Button>
                            </div>
                        </div>
                    )}
                    {feedbackId !== null && (
                        <div>
                            <div>
                                <Typography display="block" variant="h5" color={"primary"}>
                                    Tickets rate is
                                </Typography>
                            </div>
                            <br></br>
                            <div className="inputs-section__ticket-creation-input ticket-creation-input">
                                <Typography display="initial" variant="h1"
                                            color={"secondary"}>{feedbackRate}</Typography>
                            </div>
                            <br></br>
                            <br></br>
                            <div align={"center"}>
                                <Typography display="block" variant="h5" color={"primary"}>
                                    Feedback about ticket:
                                </Typography>
                            </div>
                            <div>
                                <Typography display="block" variant="h4" color={"secondary"}>{feedbackText}</Typography>
                            </div>
                            <div>
                                <br></br>
                            </div>
                        </div>
                    )}

                </div>

            </switch>

        )

    }
}

Feedback.propTypes = {
    match: PropTypes.object,
};

const FeedbackWithRouter = withRouter(Feedback);
export default FeedbackWithRouter;