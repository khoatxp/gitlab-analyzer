
import React from 'react';
import Index from '../../pages/login';
import {render} from "@testing-library/react";

describe("Login Folder", () =>{
    const mockEnqueue = jest.spyOn(require('notistack'), "useSnackbar");
    let enqueueSnackbar = jest.fn();

    beforeAll(() =>{
        mockEnqueue.mockImplementation(() => {return {enqueueSnackbar}});
    })

    it("Snapshot Index", () => {
        const { container } = render(
            <Index />
        )
        expect(container).toMatchSnapshot();
    })

})