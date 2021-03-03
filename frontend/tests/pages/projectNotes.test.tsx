import React from 'react';
import {render} from '@testing-library/react';
import Index from '../../pages/project/[projectId]/notes';

describe("Project Notes", () =>{
    const useRouter = jest.spyOn(require('next/router'), 'useRouter');
    const mockUseEffect = jest.spyOn(React, 'useEffect')
    const mockAxios = jest.spyOn(require('axios'), 'get');

    beforeEach(()=>{
        useRouter.mockClear();
        mockUseEffect.mockClear();
        mockAxios.mockClear();
    })

   it("Snapshot projectId", () => {
        useRouter.mockImplementationOnce(() => ({
            query: { projectId: 'TestId', startDateTime: '2020-08-22T15:40-05:00', endDateTime:'2021-08-22T15:40-05:00' },
        }));
        const { container } = render(
            <Index />
        )
        expect(container).toMatchSnapshot();

    })
    it("Test useEffect", ()=>{
        useRouter.mockImplementationOnce(() => ({
            query: { projectId: 'TestId', startDateTime: '2020-08-22T15:40-05:00', endDateTime:'2021-08-22T15:40-05:00' },
        }));
        render(<Index />);
        expect(mockUseEffect).toBeCalled();

    })

    it('Test axios',()=>{
        useRouter.mockImplementationOnce(() => ({
            query: { projectId: 'TestId', startDateTime: '2020-08-22T15:40-05:00', endDateTime:'2021-08-22T15:40-05:00' },
        }));
        render(<Index />);
        expect(mockAxios).toHaveBeenCalledTimes(2);
    })


})