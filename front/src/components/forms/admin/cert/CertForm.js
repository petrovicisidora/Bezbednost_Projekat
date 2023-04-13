import React,{ useState, useEffect } from 'react';
import strings from "../../../../localization";
import {  getUsers } from "../../../services/admin/UserAdminService";
import { getError, hasError } from "../../../../functions/Validation";
import { Button, TextField } from "@material-ui/core";
import Select from 'react-select'
import { deleteUser, getUsers, restoreUser } from "../../../services/admin/UserAdminService";
 


const CertForm = ({
    onSubmit,
    onCancel,
    onChange,
    errors,
    data,
    user
}

) =>{

   
 


  
 
      var options=[];
      console.log(user.role) 
      if(user.role=='ADMIN'){
      options = [
        {value: 'Root', label: 'Root'},
        {value: 'Intermediary', label: 'Intermediary'},
        {value: 'End-Entity', label: 'End-Entity'}
      ]}
      else{
         options = [
            {value: 'Intermediary', label: 'Intermediary'},
            {value: 'End-Entity', label: 'End-Entity'}]
      }
     
     return ( 
      
   
        <form id='user-form'>

            
            <TextField
                label={'Issuer name'}
                error={hasError(errors, 'subjectName')}
                helperText={getError(errors, 'subjectName')}
                fullWidth
                autoFocus
                name='subjectName'
                onChange={onChange}
                margin="normal"
                value={data.subjectName}
                disabled
            />
            <Select
                label="Certificate Type"
                name="certType"
                value={{ value: data.certType, label: data.certType }}
                options={options}
                onChange={(selectedOption) => {
                    onChange({
                      target: {
                        name: "certType",
                        value: selectedOption.value,
                      },
                    });
                  }}
            />
            <TextField
                label={'Subject name'}
                error={hasError(errors, 'name')}
                helperText={getError(errors, 'name')}
                fullWidth
                autoFocus
                name='name'
                onChange={onChange}
                margin="normal"
                value={data.name}
            />
            
            <TextField
                label={'Start Date'}
                error={hasError(errors, 'startDate')}
                helperText={getError(errors, 'startDate')}
                fullWidth
                autoFocus
                name='startDate'
                onChange={onChange}
                margin="normal"
                value={data.startDate}
            />
            <TextField
                label={'End Date'}
                error={hasError(errors, 'endDate')}
                helperText={getError(errors, 'endDate')}
                fullWidth
                autoFocus
                name='endDate'
                onChange={onChange}
                margin="normal"
                value={data.endDate}
            />
            <div className='submit-container'>
                <Button variant="contained" color="primary" onClick={onSubmit}>
                    {strings.userForm.ok}
                </Button>
                <Button variant="contained" color="secondary" onClick={onCancel}>
                    {strings.userForm.cancel}
                </Button>
            </div>
        </form>
    );
                }

export default CertForm;