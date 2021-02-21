import React from 'react';
import BackendTest from '../../components/BackendTest';
import {render} from "@testing-library/react";

describe("BackendTest", () =>{
    const mockAxios = jest.spyOn(require('axios'), 'get');
    const mockUseEffect = jest.spyOn(React, 'useEffect');
    const mockEnqueue = jest.spyOn(require('notistack'), "useSnackbar");
    let enqueueSnackbar = jest.fn();

    beforeAll(() =>{
        mockEnqueue.mockImplementation(() => {return {enqueueSnackbar}});

    })

    it("Snapshot BackendTest", () => {
        const { container } = render(
            <BackendTest />
        )
        expect(container).toMatchSnapshot();

    })
    it('Test useEffect', ()=> {
        expect(mockUseEffect).toBeCalled();
    })

    it('Test axios',()=> {
        expect(mockAxios).toHaveBeenCalledTimes(1);
    })
})