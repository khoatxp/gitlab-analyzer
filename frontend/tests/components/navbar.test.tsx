import React from 'react';
import NavBar from '../../components/NavBar';
import {mount} from "enzyme";


describe("NavBar", () =>{
    const useRouter = jest.spyOn(require('next/router'), 'useRouter');
    const pushRouter = jest.spyOn(require('next/router'), 'useRouter');
    let push = jest.fn();

    beforeAll(() => {
        useRouter.mockImplementationOnce(() => ({
            query: { projectId: 'TestId' },
        }));
        pushRouter.mockImplementation(() => {return {push}})
    });



    it("Snapshot NavBar", async() => {
        const rend = mount(
            <NavBar />
        )
        expect(rend).toMatchSnapshot();
    })

})