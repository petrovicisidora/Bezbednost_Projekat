import React, { Component } from 'react'
import strings from "../../localization";
import { SelectField } from 'material-ui';

class BaseControl extends Component {

    constructor(props) {
        super(props);
    }

    onChange(event) {

        if (!this.props.onChange) {
            return
        }

        if (!event) {
            this.props.onChange({
                target: {
                    name: this.state.name,
                    value: null
                }
            });

            return;
        }

        this.props.onChange(event);
    }

    handleSelectChange = (selectedOption) => {
        onChange({
            target: {
                name: 'certType',
                value: selectedOption.value
            }
        });
    }
}

export default BaseControl;