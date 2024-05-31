import React from "react";
import PropTypes from "prop-types";
import {
  Paper,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Typography,
} from "@material-ui/core";
import { HISTORY_TABLE_COLUMNS } from "../constants/tablesColumns";
import dateFormat from "dateformat";


function HistoryTable(props) {
  const { history } = props;

  return (
    <TableContainer component={Paper}>
      <Table>
        <TableHead>
          <TableRow>
            {HISTORY_TABLE_COLUMNS.map((item) => {
              return (
                <TableCell key={item.id} align="center">
                  <Typography variant="h6">{item.label}</Typography>
                </TableCell>
              );
            })}
          </TableRow>
        </TableHead>
        <TableBody>
          {history.map((item, index) => {
            return (
              <TableRow key={index}>
                <TableCell align="center">{dateFormat(Date.parse(item.date), "dd/mm/yyyy")}</TableCell>
                <TableCell align="center">{item.user}</TableCell>
                <TableCell align="center">{item.action}</TableCell>
                <TableCell align="center">{item.description}</TableCell>
              </TableRow>
            );
          })}
        </TableBody>
      </Table>
    </TableContainer>
  );
}

HistoryTable.propTypes = {
  name: PropTypes.array,
};

export default HistoryTable;
