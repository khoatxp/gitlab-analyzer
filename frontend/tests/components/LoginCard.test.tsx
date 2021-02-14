import React from 'react';
import LoginCard from '../../components/LoginCard';
import {render} from "@testing-library/react";

describe("LoginCard", () =>{
    const mockEnqueue = jest.spyOn(require('notistack'), "useSnackbar");
    let enqueueSnackbar = jest.fn();


    beforeAll(() =>{
        mockEnqueue.mockImplementation(() => {return {enqueueSnackbar}});
    })

    it("Snapshot LoginCard", () => {
        const { container } = render(
            <LoginCard />
        )
        expect(container).toMatchSnapshot();
    })

})